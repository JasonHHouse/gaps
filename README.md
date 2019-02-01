
# Gaps
Searches through your Plex Server for all movies, then queries for known movies in the same collection. If those movies don't exist in your library, Gaps will recommend getting those movies, legally of course.

## Usage
### Properties
You need to find your plex movie all URL. Plex has a great write up on to get the URL. But first you'll need to get your token. The write ups are below.

https://support.plex.tv/articles/204059436-finding-an-authentication-token-x-plex-token/
https://support.plex.tv/articles/201638786-plex-media-server-url-commands/

It will look something like this.

http://127.0.0.1:32400/library/sections/1/all/?X-Plex-Token={My-Plex-Token}

Put that in your application.yaml plexMovieAllUrl

Then you need to go make an account on https://www.themoviedb.org and generate an API Key. Add that key to the application.yaml file under the movieDbApiKey property.

### Running
It's a Spring Boot App. I run from Intellij to make things easier but you can run
```bash
mvn clean install
mvn spring-boot:run
```
The logger will print out information about duplicate movies and movies not found. The program takes time but in the end you will get a list of movies missing from collections you have. If you have any questions, hit me up. Feel free to put up PRs to improve this as well.

## License
Copyright 2019 Jason H House

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

