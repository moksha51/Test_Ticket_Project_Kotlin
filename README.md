## Scenario

For now is just a Proof-of-concept. 
The only entity that we have now is `Project`, later on we'll introduce other entities such as `Client` and `Ticket`

A `Project` is a simple entity that has three fields

| Name          | Type    | Description                  |
|---------------|---------|------------------------------|
| `id`          | Integer | A unique identifier, i.e. PK |
| `name`        | String  | Short name                   |
| `description` | String  | Long description             |

A `Project` represented as JSON looks like this:

```json
{
    "description": "Lorem ipsum dolor sit amet",
    "id": 1,
    "name": "Project 1"
}
