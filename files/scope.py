import csv
import sys
import subprocess
import pyvisa
from time import sleep
from rigol_ds1054z import rigol_ds1054z
import matplotlib.pyplot as plt

# def retrieve_oscilloscope_data(osc):
#     pass
#     # Setup waveform data format and source. Adjust these commands based on your needs.
#     osc.write(":WAVeform:SOURce CHANnel1")
#     osc.write(":WAVeform:FORMat BYTE")
#     osc.write(":WAVeform:MODE RAW")
#     data = osc.query_binary_values(":WAV:DATA?", datatype='B')
#     return data

def write_csv_from_list_of_lists(data, file_path):
    if not data:
        print("Empty data provided.")
        return

    column_labels = data[0]

    data_without_labels = data[1:]

    with open(file_path, 'w', newline='') as csv_file:
        writer = csv.writer(csv_file)

        # Write the column labels as the header row
        writer.writerow(column_labels)

        # Write the remaining data rows
        writer.writerows(data_without_labels)

    print(f"CSV file '{file_path}' has been created successfully.")

def plot_execution_data(file_path):
    data = {}
    # Read and parse the file
    with open(file_path, 'r') as file:
        for line in file:
            # Split the line into components
            parts = line.strip().split(',')
            # The first part is the label (e.g., "Execution 0")
            label = parts[0]
            # The rest of the parts are data points
            values = [float(value) for value in parts[1:]]
            data[label] = values

    # Plotting
    fig, ax = plt.subplots()
    for label, values in data.items():
        ax.plot(values, label=label)

    ax.set_xlabel('Sample Number')
    ax.set_ylabel('Value')
    ax.set_title('Execution Data')
    ax.legend()

    plt.show()
# def save_data_to_csv(data, csv_writer, execution_number):
#     # Add an identifier for the execution at the beginning of the data section
#     csv_writer.writerow([f"Execution {execution_number}"])
#     for value in data:
#         csv_writer.writerow([value])
#     # Mark the end of this execution's data
#     csv_writer.writerow(["EXECUTION_END"])


def setup_scope(scope):
    scope.setup_timebase(time_per_div='1ms', delay='1ms')

def setup_channels(scope):
    scope.setup_channel(channel=1, on=1, offset_divs=0.0, volts_per_div=1.0, probe=1.0)
    # scope.setup_channel(channel=1, on=1, offset_divs=-1.0, volts_per_div=5.0, probe=1.0)
    scope.setup_channel(channel=2, on=1, offset_divs=0.0, volts_per_div=2.0, probe=1.0)

def main(executions, command):
    rm = pyvisa.ResourceManager()
    osc_address = 'USB0::6833::1230::DS1ZA171912518::0::INSTR'  # Adjust this to your oscilloscope's VISA address
    print(rm.list_resources('@py'))
    # osc = rm.open_resource(osc_address)
    scope = rigol_ds1054z('USB0::6833::1230::DS1ZA171912518::0::INSTR')
    setup_scope(scope)
    table = []
    c = 0
    for i in range(int(executions)):

        # scope.reset()
        # setup_scope(scope)
        # setup_channels(scope)
        result = subprocess.run(command.split(" "), capture_output=True)
        print(result.stdout)
        # subprocess.run(command.split())
        waveform = scope.get_waveform_data(channel=1,filename='dolk.csv')
        waveform2 = scope.get_waveform_data(channel=2,filename='dolk.csv')
        # scope.get_measurement(channel=1, meas_type=rigol_ds1054z.max_voltage)
        # scope.get_measurement(channel=1, meas_type=rigol_ds1054z.min_voltage)

        cleaned = (waveform.replace("'","")).split(",")
        cleaned = cleaned[1::]
        cleaned2 = (waveform2.replace("'","")).split(",")
        cleaned2 = cleaned2[1::]
        vals = []
        values = [f'Execution {str(i+1)} Total']
        for num in cleaned:
            # print(num)
            vals.append(float(num))
        values.extend(vals)
        table.insert(c,values)
        c += 1

        vals = []
        values = [f'Execution {str(i+1)} Drop']
        for num in cleaned2:
            vals.append(float(num))
        values.extend(vals)
        table.insert(c, values)
        c += 1
        # save_data_to_csv(waveform, csv_writer, i + 1)

        sleep(1)  # Adjust based on your timing needs
    write_csv_from_list_of_lists(table, 'data.csv')
    # scope.close()
    plot_execution_data('data.csv')

    #
    # fake = [0,100,128,200,255]
    # print("_________________________________________________________________")
    # print(fake)
    # scope.digital_value_to_voltage_for_chanel(fake, channel=1)
    # print(fake)
    # print("_________________________________________________________________")



if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python script.py <number_of_executions> <command>")
        its = "3"
        cmd = "java --version"
        # sys.exit(1)
    else:
        its = sys.argv[1]
        cmd = sys.argv[2]
    executions, command = its, cmd
    print(f'{executions} x {command}')
    main(executions, command)
