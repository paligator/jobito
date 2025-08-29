### Task Title
**Add web socket support,

### Goal Statement
-

-Add new config - folder /config named Scheduler. Scheduler will have function checkMergeRequest. Function will be annotated with @Scheduled.
Cron value for Scheduler will be in config. By default every 10 seconds. Function will call function from GitLabService.checkMergeRequest