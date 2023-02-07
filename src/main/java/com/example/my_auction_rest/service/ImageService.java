package com.example.my_auction_rest.service;

import com.example.my_auction_rest.entity.Image;
import com.example.my_auction_rest.entity.Lot;
import com.example.my_auction_rest.entity.User;
import com.example.my_auction_rest.exception.ImageNotFoundException;
import com.example.my_auction_rest.exception.ImageUploadException;
import com.example.my_auction_rest.exception.LotNotFoundException;
import com.example.my_auction_rest.repository.ImageRepository;
import com.example.my_auction_rest.repository.LotRepository;
import com.example.my_auction_rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// Class that implements the business logic of the images
@Service
public class ImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private ImageRepository imageRepository;
    private LotRepository lotRepository;
    private UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, LotRepository lotRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.lotRepository = lotRepository;
        this.userRepository = userRepository;
    }

    public Image uploadImageToLot(MultipartFile file, Principal principal, Long lotId) throws IOException {
        User user = getUserByPrincipal(principal);
        Lot lot = lotRepository.findLotByIdAndUserAndArchivalIsFalse(lotId, user)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found for user: " + user.getUsername()));

        Image currLotsImage = imageRepository.findByLotId(lot.getId())
                .orElse(null);
        if(currLotsImage != null){
            throw new ImageUploadException("Lot with id " + lot.getId() + " already has an image");
        }

        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setImageBytes(file.getBytes());
        image.setImageBytes(compressByte(file.getBytes()));
        image.setLotId(lot.getId());
        LOG.info("Saving image to lot with id: " + lot.getId());
        return imageRepository.save(image);
    }

    public Image getImageToLot(Long lotId){
        Image image = imageRepository.findByLotId(lotId)
                .orElseThrow(() -> new ImageNotFoundException("Image cannot be found to lot: " + lotId));

        if(!ObjectUtils.isEmpty(image)){
            image.setImageBytes(decompressBytes(image.getImageBytes()));
        }

        return image;
    }


    private static byte[] compressByte(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        LOG.info("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

//    public List<LotWithImageResponse> getLotsWithImages(List<Lot> lots){
//        List<LotWithImageResponse> lotsWithImages = new ArrayList<>();
//
//        // Search for images for each lot
//        for (Lot lot : lots) {
//            try {
//                Image image = getImageToLot(lot.getId());
//                lotsWithImages.add(new LotWithImageResponse(lot, image));
//            } catch (ImageNotFoundException ex) {
//                logger.debug(ex.getMessage());
//                lotsWithImages.add(new LotWithImageResponse(lot, (String) null));
//            }
//        }
//
//        return lotsWithImages;
//    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
    
}
