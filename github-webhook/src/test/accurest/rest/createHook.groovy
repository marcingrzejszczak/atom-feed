package rest

io.codearte.accurest.dsl.GroovyDsl.make {
	request {
		method 'POST'
		url '/'
		body '''
{
  "zen": "Avoid administrative distraction.",
  "hook_id": 8067285,
  "hook": {
    "type": "Organization",
    "id": 8067285,
    "name": "web",
    "active": true,
    "events": [
      "*"
    ],
    "config": {
      "url": "http://github-webhook.cfapps.io/",
      "content_type": "json",
      "insecure_ssl": "0"
    },
    "updated_at": "2016-04-14T10:36:54Z",
    "created_at": "2016-04-14T10:36:54Z",
    "url": "https://api.github.com/orgs/spring-cloud-samples/hooks/8067285",
    "ping_url": "https://api.github.com/orgs/spring-cloud-samples/hooks/8067285/pings"
  },
  "organization": {
    "login": "spring-cloud-samples",
    "id": 8216893,
    "url": "https://api.github.com/orgs/spring-cloud-samples",
    "repos_url": "https://api.github.com/orgs/spring-cloud-samples/repos",
    "events_url": "https://api.github.com/orgs/spring-cloud-samples/events",
    "hooks_url": "https://api.github.com/orgs/spring-cloud-samples/hooks",
    "issues_url": "https://api.github.com/orgs/spring-cloud-samples/issues",
    "members_url": "https://api.github.com/orgs/spring-cloud-samples/members{/member}",
    "public_members_url": "https://api.github.com/orgs/spring-cloud-samples/public_members{/member}",
    "avatar_url": "https://avatars.githubusercontent.com/u/8216893?v=3",
    "description": "Sample applications for Spring Cloud"
  },
  "sender": {
    "login": "dsyer",
    "id": 124075,
    "avatar_url": "https://avatars.githubusercontent.com/u/124075?v=3",
    "gravatar_id": "",
    "url": "https://api.github.com/users/dsyer",
    "html_url": "https://github.com/dsyer",
    "followers_url": "https://api.github.com/users/dsyer/followers",
    "following_url": "https://api.github.com/users/dsyer/following{/other_user}",
    "gists_url": "https://api.github.com/users/dsyer/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/dsyer/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/dsyer/subscriptions",
    "organizations_url": "https://api.github.com/users/dsyer/orgs",
    "repos_url": "https://api.github.com/users/dsyer/repos",
    "events_url": "https://api.github.com/users/dsyer/events{/privacy}",
    "received_events_url": "https://api.github.com/users/dsyer/received_events",
    "type": "User",
    "site_admin": false
  }
}
'''
	}
	response {
		status 200
	}
}