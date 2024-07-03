# A GRASP method for the Bi-Objective Multiple Row Equal Facility Layout Problem

The Bi-Objective Multiple Row Equal Facility Layout Problem considers both quantitative and qualitative objectives that are very useful in many scenarios like the factory design. In this work, a new multi-objective GRASP approach is proposed which applies an ensemble of four different constructive methods followed by the combination of two local search procedures, improving the results from the state of the art. Due to the superiority of this proposal, a new dataset of larger problem instances is generated, providing detailed metrics of the obtained solutions.

Paper link: https://doi.org/10.1016/j.asoc.2024.111897 (Open Access)


* Impact Factor: 7.2  
* Quartil: Q1 - 27/197 - Computer Science, Artificial Intelligence |
           Q1 - 15/169 - Computer Science, Interdisciplinary Applications | 2023  
* Applied Soft Computing

## Code
The entire project is in Java. All the code used is located within the Code folder.

## Instances
All txt format instances can be found in instances folder.

### Datasets
+ [A-10-10, A-10-90]
+ [A-12-10, A-12-90]
+ [A-20-10, A-20-90]
+ [A-25-10, A-25-90]
+ N-15 & N-20
+ O-10, O-15, O-20
+ S-12, S-14, S-15, S-20, S-25
+ [Y-10, Y-60]
+ Previous6, Previous8, Previous12 & Previous15
  
## Results
The algorithms are executed a total of 30 times. 
Each folder contains the execution of the 4 GRASP configurations, NSGA-II, and SPEA2. 
Each folder includes the set of non-dominated solutions obtained.
 
## Cite
Please cite our paper if you use it in your own work:

Bibtext
```
@article{Uribe2024,
title = {An improved GRASP method for the multiple row equal facility layout problem},
journal = {Expert Systems with Applications},
volume = {182},
pages = {115184},
year = {2021},
issn = {0957-4174},
doi = {https://doi.org/10.1016/j.eswa.2021.115184},
url = {https://www.sciencedirect.com/science/article/pii/S0957417421006205},
author = {Nicolás R. Uribe and Alberto Herrán and J. Manuel Colmenar and Abraham Duarte},
keywords = {Metaheuristics, GRASP, Facility location, Row layout},
abstract = {As it is well documented in the literature, an effective facility layout design of a company significantly increases throughput, overall productivity, and efficiency. Symmetrically, a poor facility layout results in increased work-in process and manufacturing lead time. In this paper we focus on the Multiple Row Equal Facility Layout Problem (MREFLP) which consists in locating a given set of facilities in a layout where a maximum number of rows is fixed. We propose a Greedy Randomized Adaptive Search Procedure (GRASP), with an improved local search that relies on an efficient calculation of the objective function, and a probabilistic strategy to select those solutions that will be improved. We conduct a through preliminary experimentation to investigate the influence of the proposed strategies and to tune the corresponding search parameters. Finally, we compare our best variant with current state-of-the-art algorithms over a set of 552 diverse instances. Experimental results show that the proposed GRASP finds better results spending much less execution time.}
}
```
## Contact us: 
Nicolás R. Uribe: nicolas.rodriguez@urjc.es <br>
Alberto Herrán: alberto.herran@urjc.es <br>
J. M. Colmenar: josemanuel.colmenar@urjc.es

 
