/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

export function hideTmdbAlertsAndSpinners() {
    document.getElementById('tmdbSaveSuccess').style.display = 'none';
    document.getElementById('tmdbSaveError').style.display = 'none';
    document.getElementById('tmdbTestSuccess').style.display = 'none';
    document.getElementById('tmdbTestError').style.display = 'none';
    document.getElementById('tmdbSpinner').style.display = 'none';
}

export function hideDeleteAlertsAndSpinners() {
    document.getElementById('deleteAllError').style.display = 'none';
    document.getElementById('deleteAllSuccess').style.display = 'none';
}

export function hideScheduleAlertsAndSpinners() {
    document.getElementById('scheduleSpinner').style.display = 'none';
    document.getElementById('scheduleSaveSuccess').style.display = 'none';
    document.getElementById('scheduleSaveError').style.display = 'none';
}

export function hidePlexAlertsAndSpinners() {
    document.getElementById('plexSpinner').style.display = 'none';
    document.getElementById('plexSaveSuccess').style.display = 'none';
    document.getElementById('plexSaveError').style.display = 'none';
    document.getElementById('plexTestSuccess').style.display = 'none';
    document.getElementById('plexTestError').style.display = 'none';
    document.getElementById('plexDeleteSuccess').style.display = 'none';
    document.getElementById('plexDeleteError').style.display = 'none';
    document.getElementById('plexDuplicateError').style.display = 'none';
}

export function hideTelegramAlertsAndSpinners() {
    document.getElementById('telegramTestSuccess').style.display = 'none';
    document.getElementById('telegramTestError').style.display = 'none';
    document.getElementById('telegramSaveSuccess').style.display = 'none';
    document.getElementById('telegramSaveError').style.display = 'none';
    document.getElementById('telegramSpinner').style.display = 'none';
}

export function hideSlackAlertsAndSpinners() {
    document.getElementById('slackTestSuccess').style.display = 'none';
    document.getElementById('slackTestError').style.display = 'none';
    document.getElementById('slackSaveSuccess').style.display = 'none';
    document.getElementById('slackSaveError').style.display = 'none';
    document.getElementById('slackSpinner').style.display = 'none';
}

export function hidePushBulletAlertsAndSpinners() {
    document.getElementById('pushBulletTestSuccess').style.display = 'none';
    document.getElementById('pushBulletTestError').style.display = 'none';
    document.getElementById('pushBulletSaveSuccess').style.display = 'none';
    document.getElementById('pushBulletSaveError').style.display = 'none';
    document.getElementById('pushBulletSpinner').style.display = 'none';
}

export function hideGotifyAlertsAndSpinners() {
    document.getElementById('gotifyTestSuccess').style.display = 'none';
    document.getElementById('gotifyTestError').style.display = 'none';
    document.getElementById('gotifySaveSuccess').style.display = 'none';
    document.getElementById('gotifySaveError').style.display = 'none';
    document.getElementById('gotifySpinner').style.display = 'none';
}

export function hideEmailAlertsAndSpinners() {
    document.getElementById('emailTestSuccess').style.display = 'none';
    document.getElementById('emailTestError').style.display = 'none';
    document.getElementById('emailSaveSuccess').style.display = 'none';
    document.getElementById('emailSaveError').style.display = 'none';
    document.getElementById('emailSpinner').style.display = 'none';
}

export function hideAllAlertsAndSpinners() {
    hideTmdbAlertsAndSpinners();
    hideDeleteAlertsAndSpinners();
    hideScheduleAlertsAndSpinners();
    hidePlexAlertsAndSpinners();
    hideTelegramAlertsAndSpinners();
    hideSlackAlertsAndSpinners();
    hidePushBulletAlertsAndSpinners();
    hideGotifyAlertsAndSpinners();
    hideEmailAlertsAndSpinners();
}