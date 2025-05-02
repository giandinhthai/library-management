package com.example.buildingblocks.cqrs.handler;

import com.example.buildingblocks.cqrs.request.Request;

public interface RequestHandler<T extends Request<R>, R> {
    R handle(T request);
}