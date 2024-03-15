# "Token Server" Learning Endpoint: Unreliable-by-Design 

Welcome to the Token Server


                       ##
                      _[]_
                     [____]
                 .----'  '----.
             .===|    .==.    |===.
             \   |   /####\   |   /
             /   |   \####/   |   \
             '===|    `""`    |==='
             .===|    .==.    |===.
             \   |   /::::\   |   /
             /   |   \::::/   |   \
             '===|    `""`    |==='
             .===|    .==.    |===.
             \   |   /&&&&\   |   /
             /   |   \&&&&/   |   \
             '===|    `""`    |==='
                 '--.______.--'


# Token Server: There can be only one

This server's tokens support run-once execution. You begin by locking the activity with a unique identifier. You receive a key that enables your activity to complete or (in the case of error) reset. These endpoints are easily integrated into your application's workflow to provide a simple approach for run-once execution. Locks ensure you don't end up ordering a dozen pizzas or sending a hundred emails when you meant for your process to be run only once.

# Endpoints:

* `GET /lock?id=lock_id` - Locks a new activity and returns an API key that enables 'unlock' and 'complete' for this process_id.

* `GET /unlock?id=lock_id&key=generated_key` - Unlocks a failed activity using the provided key and lock id.

* `GET /complete?id=lock_id&key=generated_key` - Marks a successful activity as complete. Subsequent calls to this endpoint are not allowed.

# Explanation and Examples:

## Lock a New Activity

This endpoint locks a new activity and returns a key.

GET /lock?id=lock_id
For example

```
$ curl http://localhost:8409/lock?id=my_process
abc123
$
```

This call fails when the lock identifier is already in use or has been completed.

## Unlock a Failed Activity

This endpoint unlocks a failed activity using the provided key and lock id.

```
GET /unlock?id=lock_id&key=generated_key
```

This call fails when the lock identifier is unknown, when the key is incorrect, or the activity been completed.

## Complete a Successful Activity

This endpoint marks a successful activity as complete. Subsequent calls to update a completed lock are not allowed. The call also fails when the lock_id or key aren't recognized.

```
GET /complete?id=lock_id&key=generated_key
```

# Overview

```
┌───────┐ Try to   ┌─────────┐
│       │   Lock   │ already │
│ Start ├──┬───────┤ locked  ├───────►FAIL
│       │  │       ├─────────┤
└───────┘  │       ├─────────┤
           │       │already  ├───────►FAIL
┌───────┐  │       │complete │         ▲ ▲
│       │  │       └─────────┘         │ │
│locked ◄──┘                           │ │
│       │                              │ │
│ (key) │                              │ │
│       ├──────────────┐               │ │
└───┬───┘              │               │ │
    │                  │               │ │
    │ Activity         │ Activity      │ │
    │ Failure          │ Completes     │ │
┌───▼───────┐      ┌───▼──────┐        │ │
│           │      │          │        │ │
│ Unlock    │      │ Complete ├────────┘ │
│ with key  │      │ with key │  Lock or │
│           │      │          │  Complete│
└────┬──────┘      └──────────┘          │
     │                                   │
     │      Lock or Complete             │
     └───────────────────────────────────┘
```