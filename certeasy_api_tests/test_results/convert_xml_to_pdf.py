import subprocess

# Path to your XML file
xml_file = 'test_results.xml'

# Path to your XSLT stylesheet
xslt_file = 'stylesheet.xsl'

# Path to output PDF file
pdf_file = 'test_results.pdf'

# Transform XML to XSL-FO using XSLT
subprocess.run(['xsltproc', '--output', 'output.fo', xslt_file, xml_file])

# Generate PDF from XSL-FO using Apache FOP
subprocess.run(['fop', '-fo', 'output.fo', '-pdf', pdf_file])
