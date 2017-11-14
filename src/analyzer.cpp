#include <iostream>

#include <TFile.h>
#include <TTree.h>

#define NUM 100
#define NUM_EVENTS 10000

int main(int argc, char** argv) {
    std::cout << "Hello World" << std::endl;

    TFile *f = new TFile("test.root");
    TTree *t = (TTree*)f->Get("TestIO");
    double darr[NUM][NUM];
    t->SetBranchAddress("darr", &darr);

    for (auto i=0; i<NUM_EVENTS; i++) {
        if (i%100 == 0)
            std::cout << "Event = " << i << " is being analyzed" << std::endl; 

        t->GetEntry(i);
    }

    std::cout << "Bye Bye World" << std::endl;
}
