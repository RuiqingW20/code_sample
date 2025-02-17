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
Molecular dynamics (MD) simulations were performed using OpenMM to study the structural and energetic properties of a molecular system. The system was initialized from a PDB file and simulated with a force field without nonbonded cutoff constraints. After energy minimization, equilibration at 150 K was conducted, followed by a 20 ns production run at 298.15 K using a Langevin integrator. Key thermodynamic properties, including potential energy and temperature, were monitored, and trajectory data was recorded. 

This simulation provides analysis on butane’s geometry by computing histograms and PMFs for C–C and C–H bond lengths, H–C–C–H torsion angles, and C–C–C bond angles, revealing its conformational and energetic properties.

### Ubiquitin simulation trajectory analysis 
This sample work is to analyze the simulation result from  1 ms simulation Ubiquitin using Anton([anton](https://dl.acm.org/doi/10.1145/1654059.1654126)).The script shows on load the trajectory, analyze the Radius of gyration, RMSD, hydrogen bond distribution, dihedral angles of backbone and PCA anlysis on this protein. A contact map and contact distances for a protein trajectory was identified by computing the contact distance between each pair using md modules. PCA analysis used the Cartesian coordinates and plots the first two principal components (PC1, PC2) colored by RMSD.  The RMSF for minor and major states of the protein and free energy surface is plotted based on PC1 and PC2 values.

For furhtur info refer to this paper ([ubiquitin analysis](https://pubs.acs.org/doi/abs/10.1021/acs.jpcb.6b02024)).

### protein aggregation model define 
To fit the aggregation value from the experiment result, we chose the most common sigmodal model. In order to understand the physical knetics of the aggregation formation, a reaction of k1, and k2 were used to get the information of lag phase and growth phase curve and induction point were calculated based on the model. 

