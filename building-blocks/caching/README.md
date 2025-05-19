building-blocks/caching/
└── src/main/java/com/example/buildingblocks/caching/
├── config/
│   └── RedisConfig.java       # Single config file for everything
├── service/
│   ├── CacheService.java      # Interface
│   └── impl/
│       └── RedisCacheService.java
└── constants/
└── CacheNames.java        # Cache name constants