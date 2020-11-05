## 1.0.0 (under development)
This is the initial version setting up the base system

- `NEW` Add single and list message request tracking
- `NEW` Add REST parameter validations
- `NEW` Add max page size to 50
- `NEW` Add 0.0.0 as unsupported version
- `NEW` Add region code to message and ability to find messages by region code
- `NEW` Add region resource to find regions by region code or location
- `NEW` Add REST method to return a region hierachy by ars or location
- `NEW` Basic region model
- 'CNG' Change to Java 11
- `NEW` Add Message resource to find a message by its identifier 
- `CNG` Resource path 'api/message' is now 'api/messages'
- `NEW` Add geocode md5 hash to improve find 
- `NEW` Change Geocode to entity
- `NEW` Add message event type "Disposed" and handler
- `CNG` Geocode field as Multipolygon
- `NEW` Implemente update and supersede messages service methods
- `CNG` Message service returns only messags with message type published
- `NEW` Add MessageStatus field to message model
- `NEW` Rest message request is sorted by publishedAt in descending order
- `NEW` Add publishedAt field to message model
- `CNG` Renamed message event type: Published -> Created
- `NEW` Message service queries point within geocode if present
- `NEW` Add point (lang & lat) query parameters to REST message request 
- `NEW` Enhance message model with geocode field
- `NEW` Add Api resource to check if API version is supported 
- `NEW` Add accept header for API versioning
- `NEW` Message rest returns date in ISO 8601 format 
- `NEW` Set timezone to UTC
- `NEW` Add Event handler consumes "type: Published" 
- `NEW` Rest resource to request messages (with an optional page request)
- `NEW` Create and read messages 
- `NEW` Basic message model

## Legend

- `NEW` means a new feature
- `CNG` means changed behavior
- `FIX` means a bugfix or fix of a glitch
- `REM` means a removed feature