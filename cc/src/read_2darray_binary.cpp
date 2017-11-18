#include <iostream>
#include <fstream>

#define NUM_VALUES 100
#define NUM_ROWS 50 * 1000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    std::string fileName = argv[1];

    std::ifstream file(fileName.c_str(), std::ios::in | std::ios::binary);
    double *darr;
    char buffer[NUM_VALUES*NUM_VALUES * 8];

    double totalSum = 0;
    for (auto i=0; i<NUM_ROWS; i++) {
        if (i%100 == 0)
            std::cout << "Event = " << i << " is being analyzed" << std::endl; 


        file.read(buffer, NUM_VALUES*NUM_VALUES*8);
        darr = reinterpret_cast<double*>(buffer);
        for (auto ii=0; ii<NUM_VALUES*NUM_VALUES; ii++)
                totalSum += darr[ii];
    }

    std::cout << "sum = " << totalSum << std::endl;

    std::cout << "Bye Bye World" << std::endl;
}
