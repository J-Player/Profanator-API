package api.mappers;

import api.models.dtos.TradeDTO;
import api.models.entities.Trade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    TradeMapper INSTANCE = Mappers.getMapper(TradeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Trade toTrade(TradeDTO tradeDTO);

    TradeDTO toTradeDTO(Trade trade);

}
