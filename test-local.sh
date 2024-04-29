#!/bin/bash
pip install --upgrade pip
pip install -r certeasy-api-tests/requirements.txt
pytest-3 certeasy-api-tests/src/tests --junitxml=certeasy-api-tests/target/report.xml