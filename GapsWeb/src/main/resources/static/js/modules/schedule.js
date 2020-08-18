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


import {hideAllAlertsAndSpinners} from "./alerts-manager.min.js";
import {Payload} from "./payload.min.js";

export async function saveSchedule() {
    hideAllAlertsAndSpinners();

    const body = {};
    body.schedule = document.getElementById('setSchedule').value;
    body.enabled = document.getElementById('scheduleEnabled').value;

    let response = await fetch(`/schedule`, {
        method: 'put',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    })
    const put = await response.json();
    if (put.code && put.code === Payload.SCHEDULE_UPDATED) {
        hideAllAlertsAndSpinners();
        document.getElementById('scheduleSaveSuccess').style.display = 'block';
    } else {
        hideAllAlertsAndSpinners();
        document.getElementById('scheduleSaveError').style.display = 'block';
    }
}
