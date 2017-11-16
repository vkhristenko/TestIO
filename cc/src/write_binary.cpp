#include <iostream>
#include <fstream>

// define buffer size in bytes
#define BUFFER_SIZE 100

// define the number of buffers to be written
#define NUM_BUFFERS 1000 * 1000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    std::ofstream file("test_binary.bin", std::ios::out | std::ios::binary);

    char buffer[BUFFER_SIZE];

    for (auto i=0; i<NUM_BUFFERS; i++) {
        for (auto j=0; j<BUFFER_SIZE; j++) {
            buffer[j] = i%256;
        }
        file.write(buffer, BUFFER_SIZE);
    }

    file.close();

    std::cout << "Bye Bye World" << std::endl;
}
