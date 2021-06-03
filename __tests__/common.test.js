jest.mock('../__mocks__/getRecommendedMovies');
import { getYear, isEqual, isNotOwned, getRecommendedMoviesForTable } from '../GapsWeb/src/main/resources/static/js/modules/common';

test('Checks for valid year', () => {
  expect(getYear(2005)).toBe(' (2005)');
});

test('Checks for year 0', () => {
  expect(getYear(0)).toBe('');
});

test('Checks for year -1', () => {
  expect(getYear(-1)).toBe('');
});

test('Checks for no year', () => {
  expect(getYear()).toBe('');
});

test('Two equal TMDB IDs', () => {
  expect(isEqual(1234567,1234567)).toBe(true);
});

test('Two unequal TMDB IDs', () => {
  expect(isEqual(1234567,7654321)).toBe(false);
});

test('Should be owned', () => {
  expect(isNotOwned(true)).toBe(false);
});

test('Should not be owned', () => {
  expect(isNotOwned(false)).toBe(true);
});

// async/await can also be used with `.resolves`.
it('works with async/await and resolves', async () => {
  expect.assertions(1);
  await expect(getRecommendedMoviesForTable()).resolves.toEqual();
});