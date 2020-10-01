/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/* global cy, it, expect, describe, before, beforeEach */
/* eslint no-undef: "error" */

import {
  nuke, redLibraryBefore, searchPlexForMoviesFromSaw, spyOnAddEventListener,
} from '../common.js';

function searchSawLibrary(cy) {
  cy.visit('/libraries', { onBeforeLoad: spyOnAddEventListener });

  searchPlexForMoviesFromSaw(cy);

  cy.visit('/recommended', { onBeforeLoad: spyOnAddEventListener });
}

describe('Library API', () => {
  it('Get bad library', () => {
    cy.request('/libraries/abc/123')
      .then((resp) => {
        cy.log(resp.body);
        const result = resp.body;
        expect(result.code).to.eq(41);
      });
  });

  before(nuke);
  before(redLibraryBefore);

  it('Get library Red - Saw', () => {
    searchSawLibrary(cy);

    cy.request('/libraries/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
      .then((resp) => {
        cy.log(resp.body);
        const result = resp.body;
        expect(result.code).to.eq(40);
      });
  });
});

describe('Plex Movie List API', () => {
  beforeEach(nuke);
  beforeEach(redLibraryBefore);

  it('Get library Red - Saw', () => {
    cy.request('/plex/movies/721fee4db63634b88ed699f8b0a16d7682a7a0d9/2')
      .then((resp) => {
        cy.log(resp.body);
        const result = resp.body;
        expect(result).to.have.lengthOf(1);
        expect(result[0].imdbId).to.eq('tt0387564');
      });
  });
});
