import random
import torch
import time

# Goal : Fine-tuning the parameters of the hugging-face model and 
#        try to generate more than 20 paraphrased sentences per input

# Step 1 : Implement a metric score of the paraphrased sentences to try and optimize a number.
# Step 2 : Implement some sort of grid search that gives the best parameters for the model.
#          the best parameters are those that maximize the metric score.

from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
tokenizer = AutoTokenizer.from_pretrained("humarin/chatgpt_paraphraser_on_T5_base")

model = AutoModelForSeq2SeqLM.from_pretrained("humarin/chatgpt_paraphraser_on_T5_base").to(device)


def paraphrase(
        question,
        num_beams=20, # num_beams=20
        num_beam_groups=20, # num_beam_groups=20
        num_return_sequences=20, # num_return_sequences=20
        repetition_penalty=13.0, # repetition_penalty=1.5 #10.0
        diversity_penalty=4.0, # diversity_penalty=0.5 #3.0
        no_repeat_ngram_size=2, # no_repeat_ngram_size=3 #2
        temperature=0.7, # temperature=0.7
        max_length=128 # max_length=128
):
    input_ids = tokenizer(
        f'paraphrase: {question}',
        return_tensors="pt", padding="longest",
        max_length=max_length,
        truncation=True,
    ).input_ids.to(device)

    outputs = model.generate(
        input_ids.to(device), temperature=temperature, repetition_penalty=repetition_penalty,
        num_return_sequences=num_return_sequences, no_repeat_ngram_size=no_repeat_ngram_size,
        num_beams=num_beams, num_beam_groups=num_beam_groups,
        max_length=max_length, diversity_penalty=diversity_penalty
    )

    res = tokenizer.batch_decode(outputs, skip_special_tokens=True)

    return res


def paraphrase_time(sentence):
    start_time = time.time()
    result = paraphrase(sentence)
    end_time = time.time()
    execution_time = end_time - start_time
    print("Execution Time:", execution_time, "seconds")
    return result

# Testing 
if __name__ == '__main__':
    print(paraphrase_time("I have no idea."))