package com.example.buildingblocks.cqrs.mediator;

import com.example.buildingblocks.cqrs.request.Request;

public interface Mediator {
    <R, T extends Request<R>> R send(T request);
}