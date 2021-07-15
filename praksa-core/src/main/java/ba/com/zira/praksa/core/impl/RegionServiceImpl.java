package ba.com.zira.praksa.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ba.com.zira.commons.message.request.ListRequest;
import ba.com.zira.commons.message.response.ListPayloadResponse;
import ba.com.zira.commons.model.response.ResponseCode;
import ba.com.zira.praksa.api.RegionService;
import ba.com.zira.praksa.api.model.LoV;
import ba.com.zira.praksa.dao.RegionDAO;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionDAO regionDAO;

    @Override
    public ListPayloadResponse<LoV> lovs(ListRequest<Long> request) {
        List<LoV> loVs = regionDAO.findNamesForIds(request.getList());

        return new ListPayloadResponse<>(request, ResponseCode.OK, loVs);
    }

}
