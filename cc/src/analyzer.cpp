#include <iostream>

#include <TFile.h>
#include <TTree.h>

#define NUM 100
#define NUM_EVENTS 50000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    std::string fileName = argv[1];

    TFile *f = new TFile(fileName.c_str());
    TTree *t = (TTree*)f->Get("TestIO");
    double darr[NUM][NUM];
    t->SetBranchAddress("darr", &darr);

    double totalSum = 0;
    for (auto i=0; i<NUM_EVENTS; i++) {
        if (i%100 == 0)
            std::cout << "Event = " << i << " is being analyzed" << std::endl; 

        t->GetEntry(i);
        for (auto ii=0; ii<NUM; ii++)
            for (auto jj=0; jj<NUM; jj++)
                totalSum += darr[ii][jj];
    }

    std::cout << "sum = " << totalSum << std::endl;

    std::cout << "Bye Bye World" << std::endl;
}
