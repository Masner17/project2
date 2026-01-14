package com.masner.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masner.project2.entity.Asset;
import com.masner.project2.service.AssetService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService){
        this.assetService = assetService;
    }

    @GetMapping
    public List<Asset> getAllAssets(){
        return assetService.findAll();
    }

    @GetMapping("/active")
    public List<Asset> getActiveAssets(){
        return assetService.findAllActive();
    }

    @PostMapping
    public ResponseEntity<Asset> saveAsset(@RequestBody Asset asset){
            try {
                Asset newAsset = assetService.create(asset);
                return ResponseEntity.status(HttpStatus.CREATED).body(newAsset);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
    
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset){
        Asset assetActualizado = assetService.updateAsset(asset, id);
        return new ResponseEntity<>(assetActualizado, HttpStatus.OK);
    }

    @PutMapping("/{id}/desactive")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id){
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Asset desactivate");
    }
}
