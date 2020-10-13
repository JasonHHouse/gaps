/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

/* global Handlebars */

/* eslint no-undef: "error" */

export function openPlexLibraryConfigurationModel(title, machineIdentifier, key) {
  const obj = {
    title,
    machineIdentifier,
    key,
  };
  const plexLibraryModalTemplate = $('#plexLibraryModalTemplate').html();
  const theTemplate = Handlebars.compile(plexLibraryModalTemplate);
  const theCompiledHtml = theTemplate(obj);
  const plexLibraryConfigurationModal = document.getElementById('plexLibraryConfigurationModal');
  plexLibraryConfigurationModal.innerHTML = theCompiledHtml;
  $('#plexLibraryConfigurationModal').modal('show');
}

export async function savePlexLibraryConfiguration(machineIdentifier, key) {
  const obj = {
    machineIdentifier,
    key,
    enabled: document.getElementById('libraryEnabled').value,
    defaultLibrary: document.getElementById('defaultLibrary').value,
  };

  const response = await fetch('/configuration/update/plex/library', {
    method: 'put',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(obj),
  });
  await response.json();
  /* const put = await response.json(); */
  /* if (put.code && put.code === Payload.SCHEDULE_UPDATED) {
      hideAllAlertsAndSpinners();
      document.getElementById('scheduleSaveSuccess').style.display = 'block';
  } else {
      hideAllAlertsAndSpinners();
      document.getElementById('scheduleSaveError').style.display = 'block';
  } */
}

export async function getPlexToken() {
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  const url = `https://plex.tv/users/sign_in.json?user%5Blogin%5D=${username}&user%5Bpassword%5D=${password}`;

  /* ToDo remove this and query for the value from server */
  const response = await fetch(url, {
    method: 'post',
    headers: {
      'X-Plex-Product': 'Gaps',
      'X-Plex-Version': '0.8.3',
      'X-Plex-Client-Identifier': 'b1547b7b-06db-49ab-969c-5bb8e8582d68',
    },
  });
  await response.json();
}
