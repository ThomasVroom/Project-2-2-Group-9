from fastapi import FastAPI
from pydantic import BaseModel
import os

import CFGsentence
import paraphraser
import Model
import Inference

app = FastAPI()

class ParaphraseData(BaseModel):
    paraphraser_exec: bool
    paraphraser_del: bool

class TrainingData(BaseModel):
    training: bool

class InferenceData(BaseModel):
    inference: str

@app.post("/paraphrase")
async def paraphrase(data: ParaphraseData):
    directory_path = "Data/"
    if data.paraphraser_del:
        file_list = os.listdir(directory_path)
        for file_name in file_list:
            file_path = os.path.join(directory_path, file_name)
            if os.path.isfile(file_path):
                os.remove(file_path)
                print(f"Deleted file: {file_name}")
            else:
                print(f"Skipped deletion: {file_name} (not a file)")
    if data.paraphraser_exec:
        setoflabels = CFGsentence.getStuff()
        for sentence, labels in setoflabels:
            if labels != "I have no idea." and not os.path.exists(directory_path + labels + ".txt"):
                sentences = paraphraser.paraphrase(sentence)[0]
                sentences.append(sentence)
                with open(directory_path + labels + ".txt", "a+") as file:
                    file.seek(0)
                    is_empty = len(file.read()) == 0
                    file.seek(0, 2)
                    if not is_empty:
                        file.write("\n")
                    for string in sentences:
                        file.write(string + "\n")
    return {"status": "Paraphrase completed"}

@app.post("/train")
async def train(data: TrainingData):
    if data.training:
        Model.train()
    return {"status": "Training completed"}

@app.post("/infer")
async def infer(data: InferenceData):
    if data.inference != None:
        result = Inference.inferclass(data.inference)
        return {"result": result}
    else:
        return {"error": "No inference data provided"}
