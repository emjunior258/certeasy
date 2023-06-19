import json
import os


def file_reader(filename: str):
    script_dir = os.path.dirname(__file__)  # <-- absolute dir the script is in
    abs_file_path = os.path.join(script_dir, filename)
    f = open(abs_file_path)
    data = json.load(f)
    f.close()
    return data

