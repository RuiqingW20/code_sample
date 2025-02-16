# code_sample
 this is the code sample for potential interview by Ruiqing Wang. 
 
 ## Job Compare 
JobCompare is an Android application designed to help users evaluate and compare job offers based on multiple financial and non-financial factors. The app provides a structured way to input job-related details, such as salary, bonuses, benefits, and cost of living, to make informed career decisions.

This project follows Android development best practices and includes both frontend and backend components. The application is built to run on Android 12+ devices and is designed for mobile phones only. All data is stored client-side to ensure offline access and quick retrieval.

To ensure reliability, the project includes unit tests and Android tests, validating core functionalities and UI behaviors. Given the development timeline constraints, simplicity and efficiency were prioritized in the design and implementation.
### Application preview
Here is the main menu screen: 

![mainscreen](./pictures/MainMenu.png)

Here is the short video on running this app: 

![demo](./pictures/demo_fast.gif)

## MD_Analysis
### OpenMM test on butane 
Molecular dynamics (MD) simulations were performed using OpenMM to study the structural and energetic properties of a molecular system. The system was initialized from a PDB file and simulated with a force field without nonbonded cutoff constraints. After energy minimization, equilibration at 150 K was conducted, followed by a 20 ns production run at 298.15 K using a Langevin integrator. Key thermodynamic properties, including potential energy and temperature, were monitored, and trajectory data was recorded. This simulation provides insights into the system’s stability and thermodynamic behavior.
