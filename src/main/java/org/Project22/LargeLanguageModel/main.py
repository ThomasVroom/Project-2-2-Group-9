import logging
import os
import sys

import CFGsentence
import paraphraser
import Model
import time

def main(training=False, paraphraserexec=False, paraphraserdel=False, inference=None):
    if paraphraserexec:
        directory_path = "Data/"
        if paraphraserdel:
            file_list = os.listdir(directory_path)
            for file_name in file_list:
                file_path = os.path.join(directory_path, file_name)
                if os.path.isfile(file_path):
                    os.remove(file_path)
                    print(f"Deleted file: {file_name}")
                else:
                    print(f"Skipped deletion: {file_name} (not a file)")
        setoflabels = CFGsentence.getStuff()
        for sentence, labels in setoflabels:
            if labels != "I have no idea.":
                sentences = paraphraser.paraphrase_20(sentence)
                sentences.append(sentence)
                with open(directory_path + labels + ".txt", "a+") as file:
                    file.seek(0)
                    is_empty = len(file.read()) == 0
                    file.seek(0, 2)
                    if not is_empty:
                        file.write("\n")
                    for string in sentences:
                        file.write(string + "\n")
    if training:
        Model.train(size=(64, 64, 64), learningrate=0.01)
    if inference is not None:
        import Inference
        Inference.inferclass(inference)


if __name__ == '__main__':
    start_time = time.time()
    # Parse command-line arguments
    args = sys.argv[1:]
    training = 'training' in args
    paraphraserexec = 'paraphraserexec' in args
    paraphraserdel = 'paraphraserdel' in args
    inference = None
    if 'inference' in args:
        inference_index = args.index('inference')
        if inference_index < len(args) - 1:
            inference = args[inference_index + 1]
    # Call the main function with the provided arguments
    main(training=training, paraphraserexec=paraphraserexec, paraphraserdel=paraphraserdel, inference=inference)

    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Total time: {elapsed_time} seconds")
