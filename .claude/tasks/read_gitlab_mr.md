### Task Title
**Create new api to get gitLab merge request waiting for my approval** 

### Goal Statement
-implement new service in /service folder GitlabService.
Add model object MergeRequest, with properties name, url
Service will have function waitingForMe.
waitingForMe will call gitLab api {gitlab_base_url}/merge_requests?state=opened&reviewer_id={gitlab_user_id}
and returns list of merge request as list of objects MergeRequest
gitlab_base_url and gitlab_user_id are variable taken from application config

-Implement new routing api /gitlab/waiting-for-me
call GitlabService.waitingForMe and returns it to client as json object
{ merge_request_waiting: [
        "url": "merge_request_url",
        "name": "merge_request_name"
    ]
}

