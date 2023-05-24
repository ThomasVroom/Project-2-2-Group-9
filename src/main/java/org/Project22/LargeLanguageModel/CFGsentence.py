import random
import re

def read_rules(filename):
    rules = {}
    actions = {}

    with open(filename, 'r') as file:
        for line in file:
            line = line.strip()

            if line.startswith("Rule"):
                _, lhs, rhs = line.split(" ", 2)
                rules[lhs] = [rule.strip() for rule in rhs.split("|")]

            elif line.startswith("Action"):
                _, action = line.split(" ", 1)
                action = action.replace("*", "").strip()
                action_parts = action.split("-")
                action_pattern = action_parts[0].strip()
                action_answer = action_parts[1].strip() if len(action_parts) > 1 else ""
                actions[action_pattern] = action_answer

    return rules, actions


def generate_sentence(rules, symbol="<S>"):
    if symbol not in rules:
        return symbol, []

    production = random.choice(rules[symbol])

    sentence = ""
    labels = []

    for token in production.split():
        if token.startswith("<"):
            generated, encountered_labels = generate_sentence(rules, token)
            sentence += generated + " "
            labels.append(token)  # Append the label token
            labels.extend(encountered_labels)
        else:
            sentence += token + " "
            labels.append(token)

    return sentence.strip(), labels


def generate_unique_sentences(rules, actions, symbol="<S>", max_tries=1000):
    unique_sentences = set()

    for _ in range(max_tries):
        sentence, labels = generate_sentence(rules, symbol)
        labels = ' '.join(labels)

        matched_action = False
        for action_pattern, action_answer in actions.items():
            action_words = action_pattern.split()
            if set(action_words).issubset(set(labels.split())):
                labels = action_answer
                matched_action = True
                break

        if not matched_action:
            labels = "unknown"

        if labels:
            unique_sentences.add((sentence, labels))

    return list(unique_sentences)


def getStuff():
    rules, actions = read_rules("CFG.txt")
    unique_sentences = generate_unique_sentences(rules, actions, "<ACTION>")
    return unique_sentences

