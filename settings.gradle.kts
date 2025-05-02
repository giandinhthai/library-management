rootProject.name = "my-project"

// Building Blocks
include("building-blocks:cqrs")
include("building-blocks:event-bus")
include("building-blocks:jpa")
include("building-blocks:security")
include("building-blocks:shared-kernel")
include("building-blocks:caching")
include("building-blocks:resilience")

// Auth Service
include("services:auth-service:api")
include("services:auth-service:application")
include("services:auth-service:domain")
include("services:auth-service:infra")
//include("services:auth-service:app")


// loan-management-service modules
include(":services:borrow-book-service:api")
include(":services:borrow-book-service:application")
include(":services:borrow-book-service:domain")
include(":services:borrow-book-service:infra")
//include(":services:borrow-book-service:app")
