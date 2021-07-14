<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![GPL License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url1]
[![LinkedIn][linkedin-shield]][linkedin-url2]



<!-- TABLE OF CONTENTS -->
<details close="close">
  <summary><b>Table of Contents</b></summary>
  <ol>
    <li>
      <a href="#alc-reasoner">ALC Reasoner</a>
      <ul>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## ALC Reasoner

Realization of a reasoner that takes as input a concept (ALC), which is provided as input through a serialization of OWL/OWL (remember that owl-APIs take serializations and automatically create the Java data structures needed for the computation), which it returns true, if the concept is satisfiable, or false, if it is unsatisfiable.

The reasoner is PSPACE (i.e. use at least those forms of visits in depths that release memory, so that you are not in an EXPSPACE context).
Algorithm used is Dependency-directed backtracking.

To verify the correctness of the algorithm, a battery of tests has been implemented consisting of a minimum of 30 concepts, the results of which will be compared to the results obtained by <a href=http://www.cs.ox.ac.uk/boris.motik/pubs/smh08HermiT.pdf>HermiT</a>, a well-known reasoner whose correctness is already known.

A <a href=https://github.com/0xUrbz/ALC-Reasoner/blob/main/Presentation%20(ITA)/Sviluppo%20di%20un%20reasoner%20di%20ALC%20DL%20basato.pptx>presentation</a> is available (**only in Italian**) in which there is a detailed explanation of the algorithm, the main functions and the use of the GUI.

<!-- GETTING STARTED -->
## Getting Started
### Prerequisites

* <a href=https://www.java.com/it/download/manual.jsp>java</a>

<!-- USAGE EXAMPLES -->
## Usage

Download repo and execute jar file (ALC-Reasoner/out/artifacts/UrbanoReasoner_jar/) or recompile sources in IDE (i.e. Eclipse / JetBrains ecc.) 


<!-- CONTRIBUTING -->
## Contributing
Luigi Urbano, <a href="https://github.com/0xUrbz">0xUrbz</a><br />
Marco Urbano, <a href="https://marcourbano.me">marcourbano.me</a>

## Note
This repository corresponds to a university project for the Semantic Web course.<br />
The aim is to provide other students or researchers with a basis for implementing their projects.<br /><br />

This repository is not intended as the best solution to the problem.<br />
<b>Problems or reports are unlikely to be followed up by contributors.</b><br />
It is hoped that anyone who wants to implement a better version of the project can fork and work on it.<br />

<!-- LICENSE -->
## License

Distributed under the GPL-3.0 License. See `LICENSE` for more information.


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/0xUrbz/ALC-Reasoner.svg?style=for-the-badge
[contributors-url]: https://github.com/0xUrbz/ALC-Reasoner/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/0xUrbz/ALC-Reasoner.svg?style=for-the-badge
[forks-url]: https://github.com/0xUrbz/ALC-Reasoner/network/members
[stars-shield]: https://img.shields.io/github/stars/0xUrbz/ALC-Reasoner.svg?style=for-the-badge
[stars-url]: https://github.com/0xUrbz/ALC-Reasoner/stargazers
[issues-shield]: https://img.shields.io/github/issues/0xUrbz/ALC-Reasoner.svg?style=for-the-badge
[issues-url]: https://github.com/0xUrbz/ALC-Reasoner/issues
[license-shield]: https://img.shields.io/github/license/0xUrbz/ALC-Reasoner.svg?style=for-the-badge
[license-url]: https://github.com/0xUrbz/ALC-Reasoner/blob/master/LICENSE
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url1]: https://linkedin.com/in/luigiurbano
[linkedin-url2]: https://www.linkedin.com/in/urbanomarco/
