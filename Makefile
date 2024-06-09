setup:
	make -C app setup

clean:
	make -C app clean

build:
	make -C app build

lint:
	make -C app lint

test:
	make -C app test

report:
	make -C app report

check-updates:
	make -C app check-updates

.PHONY: build