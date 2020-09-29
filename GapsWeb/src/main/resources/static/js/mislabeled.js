/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

jQuery(($) => {
  $('#mislabeled').DataTable({
    ajax: {
      url: 'mislabeled/c51c432ae94e316d52570550f915ecbcd71bede8/1/.75',
      dataSrc: '',
    },
    columns: [
      { data: 'plexTitle' },
      { data: 'filename' },
      { data: 'percentageMatch' },
    ],
    columnDefs: [
      {
        type: 'string',
        targets: [0, 1],
      },
      {
        targets: [2],
        type: 'num-fmt',
        render(data, type, row) {
          if (type === 'display') {
            return `${Math.floor(row.percentageMatch * 100)}%`;
          }
          return '';
        },
      },
    ],
  });
});
