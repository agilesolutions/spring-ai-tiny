package com.agilesolutions.poc.dto;

import lombok.Builder;

@Builder
public record StockResponse(Float price) {
}
