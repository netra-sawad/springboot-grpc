## GRPC

---
<p align="center">
    <em>gRPC is a modern open-source Remote Procedure Call (RPC) framework developed by Google. It uses HTTP/2 for transport, Protocol Buffers (protobuf) for serialization, and supports multiple programming languages.

Instead of traditional REST APIs (which use JSON over HTTP), gRPC allows clients to directly call methods on a server application on a different machine — as if it were a local object — using a strongly typed contract.
</p>

---

## Example .proto file

```console

syntax = "proto3";

option java_package = "com.ns.fwu.grpc";

option java_multiple_files = true;

option java_outer_classname="EmployeeProto";

service EmployeeService {
rpc getEmployeeDetails (EmployeeRequest) returns (EmployeeResponse);
}

message EmployeeRequest {
int32 id = 1;
}

message EmployeeResponse {
int32 id = 1;
string name = 2;
double salary = 3;
repeated Department department = 4;
map<string, string> address_map = 5;
bool isActive = 6;
bytes profile_picture = 7;
google.protobuf.Timestamp joinDate = 8;
}

message Department {
int32 id = 1;
string name = 2;
}

```


## Basic Java ↔ Proto Type Mapping

---

| Java Type | Protobuf Type |
| --------- |----------|
| `int`     | `int32`  |
| `long`    | `int64`  |
| `boolean` | `bool`   |
| `double`  | `double` |
| `float`   | `float`  |
| `String`  | `string` |
| `byte[]`  | `bytes`  |


## Data Example: JSON vs Protobuf Format

---
JSON Request/Response

```console
{
  "id": 1,
  "name": "John Doe",
  "salary": 75000,
  "department": [
    {
      "id": 101,
      "name": "Engineer"
    }
  ],
  "address_map": {
    "home": "13 Main St, Kathmandu",
    "office": "34 Main St, Kathmandu"
  },
  "isActive": true,
  "profile_picture": "jkdfjklfjhdsjfhdkjhfd",
  "joinDate": "2025-08-14T12:34:56Z"
}

```

Protobuf Binary Format

```console
1:1                      → id = 1  
2:"John Doe"             → name = "John Doe"  
3:75000                  → salary = 75000  
4:{1:101, 2:"Engineer"}  → department: [{id: 101, name: "Engineer"}]  
5:{"home":"...", "office":"..."}  
                         → address_map: {"home": "...", "office": "..."}  
6:true                   → isActive = true  
7:"jkdf..."              → profile_picture = byte[]  
8:{seconds: 1755668096}  → joinDate = Timestamp  

```
### 