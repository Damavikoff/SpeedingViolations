package com.tms.speeding.controller;

import com.tms.speeding.dto.ViolationD;
import com.tms.speeding.service.ViolationService;
import com.tms.speeding.util.ResponseObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/violations")
public class ViolationController {
    
    private final ViolationService service;

    public ViolationController(ViolationService service) {
        this.service = service;
    }

    @PostMapping
    public Iterable<ViolationD> getAll() {
        return service.getAll();
    }

    @PostMapping(params = {"id"})
    public ViolationD getAllById(@RequestParam(value = "id", defaultValue = "0") Integer id) {
        return service.getById(id);
    }
    
    @PostMapping(params = {"search"})
	public Iterable<ViolationD> getAllByString(String search) {
        return service.getAllByString(search);
	}

    @PostMapping(params = {"page"})
	public Iterable<ViolationD> getAllByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return service.getAllByPage(page, limit);
	}

    @PostMapping(params = {"search", "page"})
	public Iterable<ViolationD> getAllByPageAndString(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                                                   String search) {
        return service.getAllByPageAndString(search, page, limit);
	}

    @PostMapping(path="/save")
    public ResponseObject save(@RequestBody ViolationD object) {
        return service.save(object);
    }
}
