package br.com.sindservferpa.SindServ.Service;

import br.com.sindservferpa.SindServ.Repository.FuncRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncService {

    @Autowired
    public FuncRepository funcRepository;


}
