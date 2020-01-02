# ecg-visualization
This application consumes data populated from an ECG machine, parses the information and plots it on UI. 
- The information parsing is done by applying patterns on the raw data. Finally, a well formatted ECG record is produced which holds all events details.
- To plot the narrow and broad curves, the equation of line and curves are used.
#### Equation of line
```
y=a(x)+b
```
#### Equation of curve
```
y=a(x^2)+b(x)+c
```
- On UI, the user can select the raw data file and the processing will start immediately.
