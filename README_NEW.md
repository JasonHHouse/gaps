<p align="center">
  <a href="" rel="noopener">
 <img width=320px height=180px src="https://github.com/JasonHHouse/gaps/tree/master/images/gaps-logo-black.png" alt="Project logo"></a>
</p>

<h3 align="center">Gaps</h3>

<div align="center">

  [![Status](https://img.shields.io/badge/status-active-success.svg)]() 
  [![GitHub Issues](https://img.shields.io/github/issues/kylelobo/The-Documentation-Compendium.svg)](https://github.com/JasonHHouse/Gaps/issues)
  [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/kylelobo/The-Documentation-Compendium.svg)](https://github.com/JasonHHouse/Gaps/pulls)
  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

---

<p align="center"> Gaps searches through your Plex Server or local folders for all movies, then queries for known movies in the same collection. If those movies don't exist in your library, Gaps will recommend getting those movies, legally of course.
    <br> 
</p>

## 📝 Table of Contents
- [About](#about)
- [Getting Started](#getting_started)
- [Deployment](#deployment)
- [Usage](#usage)
- [Built Using](#built_using)
- [TODO](../TODO.md)
- [Contributing](../CONTRIBUTING.md)
- [Authors](#authors)
- [Acknowledgments](#acknowledgement)

## 🧐 About <a name = "about"></a>
Gaps is a FOSS application. An example of Gaps running would be having a copy of 'Alien (1979)' and Gaps recommending 'Aliens (1986)' and 'Alien³ (1992)' to be added to your collection.

## 🏁 Getting Started <a name = "getting_started"></a>
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See [deployment](#deployment) for notes on how to deploy the project on a live system.

### Prerequisites
What things you need to install the software and how to install them.

#### Option 1 
<code>
Gaps can run in <a href="https://www.docker.com/">Docker</a>. If you choose to run this way, you'll need a Docker environment installed. Docker has a good write up on installing Docker CE. Check it out <a href="https://docs.docker.com/install/">here</a>. Once you get docker up and running
</code>

#### Option 2
<code>
Gaps can run as an exe on Windows. GapsOnWindows can be found on the <a href="https://github.com/JasonHHouse/gaps/releases">releases</a> page. Gaps-{version}.zip.
</code>

### Installing
A step by step series of examples that tell you how to get a development env running.

With Docker installed, you'll need to now pull down the latest Gaps image. The Gaps image is hosted [here](https://hub.docker.com/r/housewrecker/gaps). To pull the image, run the following command in a terminal

If you are running an x86 machine, use this command.

```
docker pull housewrecker/gaps:latest
```

If you are running an ARM machine, use this command.

```
docker pull housewrecker/gaps:arm-latest
```

With the image pulled, now you can run the container. 

```
docker run -d -p 8484:8484 --name mygaps --expose 32400 -v /{localFolder}/gaps:/usr/data housewrecker/gaps:latest
```

End with an example of getting some data out of the system or using it for a little demo.

## 🔧 Running the tests <a name = "tests"></a>
Explain how to run the automated tests for this system.

### Break down into end to end tests
Explain what these tests test and why

```
Give an example
```

### And coding style tests
Explain what these tests test and why

```
Give an example
```

## 🎈 Usage <a name="usage"></a>
Add notes about how to use the system.

## 🚀 Deployment <a name = "deployment"></a>
Add additional notes about how to deploy this on a live system.

## ⛏️ Built Using <a name = "built_using"></a>
- [MongoDB](https://www.mongodb.com/) - Database
- [Express](https://expressjs.com/) - Server Framework
- [VueJs](https://vuejs.org/) - Web Framework
- [NodeJs](https://nodejs.org/en/) - Server Environment

## ✍️ Authors <a name = "authors"></a>
- [@kylelobo](https://github.com/kylelobo) - Idea & Initial work

See also the list of [contributors](https://github.com/kylelobo/The-Documentation-Compendium/contributors) who participated in this project.

## 🎉 Acknowledgements <a name = "acknowledgement"></a>
- Hat tip to anyone whose code was used
- Inspiration
- References