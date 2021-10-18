CREATE TABLE `products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `created` DATETIME NULL DEFAULT NOW(),
  PRIMARY KEY (`id`));

  ALTER TABLE `products`
  ADD COLUMN `price` DOUBLE NOT NULL AFTER `name`,
  ADD COLUMN `updated` DATETIME NULL AFTER `price`,
  CHANGE COLUMN `name` `name` VARCHAR(200) NOT NULL ,
  CHANGE COLUMN `created` `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE `orders` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `status` VARCHAR(45) NOT NULL DEFAULT 'NEW',
    `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`));

CREATE TABLE `order_items` (
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    PRIMARY KEY (`order_id`, `product_id`),
    INDEX `fk_order_items_products_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_order_items_orders`
      FOREIGN KEY (`order_id`)
      REFERENCES `shopping`.`orders` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
    CONSTRAINT `fk_order_items_products`
      FOREIGN KEY (`product_id`)
      REFERENCES `shopping`.`products` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);

CREATE TABLE `shopping`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(60) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));

CREATE TABLE `shopping`.`address_book` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `street1` VARCHAR(45) NOT NULL,
  `street2` VARCHAR(45) NULL,
  `post` VARCHAR(45) NOT NULL,
  `user_id` INT NOT NULL,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_address_book_users_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_address_book_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `shopping`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `shopping`.`reviews` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rating` INT NULL,
  `review` VARCHAR(200) NULL,
  `product_id` INT NOT NULL,
  `order_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_reviews_orders_idx` (`order_id` ASC) VISIBLE,
  INDEX `fk_reviews_products_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_reviews_orders`
    FOREIGN KEY (`order_id`)
    REFERENCES `shopping`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_products`
    FOREIGN KEY (`product_id`)
    REFERENCES `shopping`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);