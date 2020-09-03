call npm run minify-input-css
call npm run uglifyjs-index-js
call npm run uglifyjs-configuration-js
call npm run uglifyjs-libraries-js
call npm run uglifyjs-recommended-js
call npm run uglifyjs-common-js
call npm run uglifyjs-payload-js
call npm run uglifyjs-mislabeled-js
call npm run uglifyjs-alerts-manager-js
call npm run uglifyjs-notification-types-js
call npm run uglifyjs-telegram-notifications-js
call npm run uglifyjs-slack-notifications-js
call npm run uglifyjs-push-bullet-notifications-js
call npm run uglifyjs-gotify-notifications-js
call npm run uglifyjs-email-notifications-js
call npm run uglifyjs-push-over-notifications-js
call npm run uglifyjs-schedule-js
call mvn clean install
del GapsOnWindows\*.jar
del GapsOnWindows\README.md
copy GapsWeb\target\GapsWeb-0.7.2.jar GapsOnWindows\gaps.jar
copy README.md GapsOnWindows\