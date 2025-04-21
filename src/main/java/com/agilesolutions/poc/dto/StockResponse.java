package com.agilesolutions.poc.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record StockResponse(Float price) {
}
