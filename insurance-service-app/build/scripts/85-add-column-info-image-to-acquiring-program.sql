set search_path to 'insurance_service';

ALTER TABLE insurance_service.acquiring_program ADD column if not exists info_image_id BIGINT NULL;

ALTER TABLE insurance_service.acquiring_program
  ADD CONSTRAINT acquiring_info_info_image_id_fk
    FOREIGN KEY (info_image_id)
      REFERENCES images(id);