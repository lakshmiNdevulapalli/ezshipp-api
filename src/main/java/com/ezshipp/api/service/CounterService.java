package com.ezshipp.api.service;

import com.ezshipp.api.document.Counters;
import com.ezshipp.api.exception.BusinessException;
import com.ezshipp.api.exception.BusinessExceptionCode;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.repositories.CounterRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class CounterService {

    @Inject
    private CounterRepository counterRepository;

    public int getSequence(String _id) throws BusinessException, ServiceException {
        List<Counters> counters = counterRepository.findAll();
        if (counters.stream().anyMatch(c -> c.get_id().equals(_id)))    {
            Counters counter = counters.stream().filter(c -> c.get_id().equals(_id)).findFirst().get();
            int seq = counter.getSeq();
            counter.setSeq(++seq);
            updateSequence(counter);
            return seq;
        }

        throw new BusinessException(BusinessExceptionCode.COUNTER_SEQ_ID_NOT_FOUND);
    }

    private void updateSequence(Counters counter) throws ServiceException   {
        counterRepository.save(counter);
    }
}
