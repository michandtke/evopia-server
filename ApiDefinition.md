### Authorize user

### Get user
The first request, will be used to also check the credentials.

#### Request
``curl -X GET https://dry-lowlands-19508.herokuapp.com/profile --user USERNAME:PASSWORD``
#### Response
```json
{
  "id":13,
  "image":"assets/profiles/jeff_smaller.png",
  "tags": [
    {
      "id":1,
      "name":"Sport"
    },
    {
      "id":2,
      "name":"Beachvolleyball"
    }
  ],
  "profileChannels":[
    {
      "profileId":13,
      "id":1,
      "name":"Instagram",
      "value":"0160"
    }
  ]
}
```
#### Failure cases
- unauthorized
    ```json
      {
        "TODEFINE": "TODO"
      }
    ```

### Set user

### Upsert user tags

### Upsert user channels

### Get events

### Upsert event

### Delete event

### Add tag

### Delete tag

### Get all tags

### Add channel

### Delete channel

### Get all channels