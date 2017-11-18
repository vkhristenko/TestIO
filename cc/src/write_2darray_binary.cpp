#include <iostream>
#include <fstream>

// define buffer size in bytes
#define NUM_VALUES 100

// define the number of buffers to be written
#define NUM_ROWS 50 * 1000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    std::ofstream file("test_2d_binary.bin", std::ios::out | std::ios::binary);

    double array[NUM_VALUES][NUM_VALUES];

    for (auto i=0; i<NUM_ROWS; i++) {
        if (i % 100 == 0) std::cout << "Event = " << i << std::endl;
        for (auto ii=0; ii<NUM_VALUES; ii++)
            for (auto jj=0; jj<NUM_VALUES; jj++)
                array[ii][jj] = ii+jj;

        file.write(reinterpret_cast<char*>(array), NUM_VALUES * NUM_VALUES * sizeof(double));
    }

    file.close();

    std::cout << "Bye Bye World" << std::endl;
}
