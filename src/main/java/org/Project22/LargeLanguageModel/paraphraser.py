import itertools
import random
import torch
import time

from bert_score import score
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
tokenizer = AutoTokenizer.from_pretrained("humarin/chatgpt_paraphraser_on_T5_base")

model = AutoModelForSeq2SeqLM.from_pretrained("humarin/chatgpt_paraphraser_on_T5_base").to(device)

question = "I have no idea."

# ----------------------------------------------- #

def paraphrase(
        question,
        grid_search=False,
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

    # Making sure that the number of references (input question repeated) is the same length as the number of paraphrased sentences
    references = [question] * len(res) # Number of input question = Number of paraphrased sentences

    # We extract the F1 score from the BERTScore output (BERTScore returns Precision, Recall, F1Score)
    all_preds, _, hashcode = score(res, references, lang="en", model_type="bert-base-uncased")
    avg_scores = [s.mean(dim=0) for s in all_preds] # Averages the scores for all the paraphrased sentences into one score
    p_val = avg_scores[0].cpu().item() # Precision
    r_val = avg_scores[1].cpu().item() # Recall
    f1_val = avg_scores[2].cpu().item() # F1 Score

    if grid_search:
        return f1_val

    return res, f1_val # When doing grid search only return the F1 score otherwise return  res, f1_val

# Computes the time it takes to paraphrase a question
def paraphrase_time(question): # make sure the return of paraphrase is res, f1_val.
    start_time = time.time()
    result, f1 = paraphrase(question)
    end_time = time.time()
    execution_time = end_time - start_time
    print("Execution Time:", execution_time, "seconds")
    return result, f1

# ----------------------------------------------- #

# Let's try and select the best parameter values for the models by performing a grid search
# We will try to maximize the F1 score
# When doing grid search make sure the return of paraphrase function is f1_val.

def grid_search_best_parameters(question):

    # Grid of parameter values
    num_beams_values = [10, 30]
    num_beam_groups_values = num_beams_values # Has to be smaller or equal to num_beams
    num_return_sequences_values = num_beams_values # Has to be smaller or equal to num_beams
    repetition_penalty_values = [10.0, 16.0]
    diversity_penalty_values = [3.0, 5.0]
    no_repeat_ngram_size_values = [2, 4]
    temperature_values = [0.5, 1.0]
    max_length_values = [100, 150]

    best_f1_score = 0.0
    best_parameters = {}

    # Iterate over parameter combinations 
    for (
        num_beams,
        num_beam_groups,
        num_return_sequences,
        repetition_penalty,
        diversity_penalty,
        no_repeat_ngram_size,
        temperature,
        max_length,
    ) in itertools.product(
        num_beams_values,
        num_beam_groups_values,
        num_return_sequences_values,
        repetition_penalty_values,
        diversity_penalty_values,
        no_repeat_ngram_size_values,
        temperature_values,
        max_length_values,
    ):
        f1_score = paraphrase(
            question,
            grid_search=True,
            num_beams=num_beams,
            num_beam_groups=num_beams, # Has to be smaller or equal to num_beams
            num_return_sequences=num_beams, # Has to be smaller or equal to num_beams
            repetition_penalty=repetition_penalty,
            diversity_penalty=diversity_penalty,
            no_repeat_ngram_size=no_repeat_ngram_size,
            temperature=temperature,
            max_length=max_length,
        )

        if f1_score > best_f1_score:
            best_f1_score = f1_score
            best_parameters = {
                "num_beams": num_beams,
                "num_beam_groups": num_beam_groups,
                "num_return_sequences": num_return_sequences,
                "repetition_penalty": repetition_penalty,
                "diversity_penalty": diversity_penalty,
                "no_repeat_ngram_size": no_repeat_ngram_size,
                "temperature": temperature,
                "max_length": max_length,
            }
        
    return best_parameters, best_f1_score

# Testing 
if __name__ == '__main__':
    # print(paraphrase_time(question)) # make sure grid_search is false in paraphrase function.
    print(grid_search_best_parameters(question)) # make sure grid_search is true in paraphrase function.
