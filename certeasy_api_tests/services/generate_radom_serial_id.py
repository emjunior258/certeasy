from datetime import datetime

def generate_serial_id():
    # Get the current date and time
    current_time = datetime.now()

    # Format the timestamp as YYMMDDHHMMSS
    timestamp = current_time.strftime('%y%m%d%H%M%S')

    # Generate additional digits (you can replace this with any desired logic)
    additional_digits = '1634'

    # Combine the timestamp and additional digits
    generated_id = int(timestamp + additional_digits)
    return generated_id

