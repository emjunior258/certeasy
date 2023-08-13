import os


def pem_file_reader(pem_path):
    script_dir = os.path.dirname(__file__)  # <-- absolute dir the script is in
    abs_file_path = os.path.join(script_dir, pem_path)
    # Read the content of the PEM file
    with open(abs_file_path, "r") as pem_file:
        pem_content = pem_file.read()
    return pem_content
