date 20/04/2014
title Efficiency
-----
Efficiency
----
Some time ago I had a Valgrind training and one of the topics discussed was program efficiency.
Especially interesting is how lack of knowledge about an underlying architecture can really hit you.
Normally in object oriented world we do not care what is done down there.
And I am not talking here about languages like java or python but c++.
It is good if someone thinks which container he/she should use or about reserving memory for a vector.
Who thinks about processor's cache? Well, I will start with a small example:
<br></br>

~~~cpp
for (unsigned int i = 0; i < max_size_; ++i)
{
  for (unsigned int k = 0; k < max_size_; ++k)
  {
    tab_[i][k] = k;
  }
}
//and
for (unsigned int i = 0; i < max_size_; ++i)
{
  for (unsigned int k = 0; k < max_size_; ++k)
  {
    tab_[k][i] = k;
  }
}
~~~

What is a difference between those two "for" loops. Functionally they are the same.
Someone could say that switching "i" with "k" is not a big deal.
We just change order in which we are accessing memory location. But when you ran actual code, you will see a difference.
<br></br>

~~~cpp
#include <iostream>
#include <chrono>

typedef std::chrono::high_resolution_clock Timer;

class Measure{

public:
  Measure(unsigned int max_size) : max_size_(max_size)
  {
    tab_ = new unsigned int*[max_size_];

    for(unsigned int index = 0; index < max_size_; ++index)
    {
      tab_[index]=new unsigned int[max_size_];
    }
  }

  ~Measure()
  {
    for(unsigned int index = 0; index < max_size_; ++index)
    {
      delete tab_[index];
    }
    delete [] tab_;
  }

  void measure_time_i_k()
  {
    auto start_time = Timer::now();
    for (unsigned int i = 0; i < max_size_; ++i)
    {
      for (unsigned int k = 0; k < max_size_; ++k)
      {
        tab_[i][k] = k;
      }
    }

    auto end_time = Timer::now();
    auto diff_millis = std::chrono::duration_cast<
            std::chrono::duration<int, std::milli>>(
                end_time - start_time);

    std::cout << "With " << max_size_*max_size_
              << " elements of size " << max_size_ * max_size_ * 8/(1024 * 1024)
              << "MB for i k it took " << diff_millis.count() << " ms"
              << std::endl;
  }

  void measure_time_k_i()
  {
    auto start_time = Timer::now();
    for (unsigned int i = 0; i < max_size_; ++i)
    {
      for (unsigned int k = 0; k < max_size_; ++k)
      {
        tab_[k][i] = k;
      }
    }
    auto end_time = Timer::now();
    auto diff_millis = std::chrono::duration_cast<
                std::chrono::duration<int, std::milli>>(
                    end_time - start_time);

    std::cout << "With " << max_size_ * max_size_
              <<  " elements of size " << max_size_*max_size_ * 8/(1024 * 1024)
              << "MB for k i it took " << diff_millis.count() << " ms"
              << std::endl;
  }

private:
  unsigned int max_size_;
  unsigned int **tab_;
};

int main()
{
  Measure ms(1000);

  ms.measure_time_i_k();
  ms.measure_time_k_i();

  Measure ms2(10000);

  ms2.measure_time_i_k();
  ms2.measure_time_k_i();

  Measure ms3(20000);

  ms3.measure_time_i_k();
  ms3.measure_time_k_i();

  return 0;
}
~~~

Output:
<br></br>

~~~sh
With 1000000 elements of size 7MB for i k it took 2 ms
With 1000000 elements of size 7MB for k i it took 8 ms
With 100000000 elements of size 762MB for i k it took 346 ms
With 100000000 elements of size 762MB for k i it took 1288 ms
With 400000000 elements of size 3051MB for i k it took 1354 ms
With 400000000 elements of size 3051MB for k i it took 7679 ms
~~~

Difference is noticeable, first version is faster. What is a reason of that?
We will need to describe first from where CPU reads data on which it operates. In modern times it reads from L1 cache memory,
if given data doesn't exist there then copy it from L2 cache to L1.
This process repeats itself until it reaches external memory. E
ach of those memories are slower than previous one. I have personally Intel Core i7-3610QM with

Level 1 Cache    256 KB

Level 2 Cache	1024 KB

Level 3 Cache	6144 KB

Like we can see from collected data L1 memory is to small for table size. Processor will load to L1 only part of it.
To make things really simple we will assume that it can read only one row per load.
For measure_time_i_k() method it will fill L1 with and do max_size_ operations on it.
But for measure_time_k_i() CPU will load a first row and write a first element.
Then it will load a second row, what is equivalent with removing the first row from L1.
And then write a first element of a second row. CPU will do max_size_ * max_size_ loads instead of max_size_.
Still, on my computer this difference wasn't very dramatic. It could be worst. So, beware.