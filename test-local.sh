#!/bin/bash
pip install --upgrade pip
pip install -r certeasy_api_tests/requirements.txt
pytest-3 certeasy_api_tests/src/tests --junitxml=certeasy_api_tests/target/report.xml