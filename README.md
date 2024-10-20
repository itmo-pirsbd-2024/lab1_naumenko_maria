# Лабораторная № 1

## Раздел
Профилирование и тестирование производительности

## Описание
В данной лабораторной работе студентам предстоит реализовать один из изученных на предыдущих курсах алгоритмов или структур данных и выполнить анализ производительности полученного решения, а также серию оптимизаций алгоритма с точки зрения потребления памяти, ресурсов процессора и jit-а.

## Алгоритмы
Для реализации были выбраны алгоритмы сортировок без сравнений:
- блочная сортировка
- сортировка подсчетом

## Сортировка подсчетом
### Худший случай (ветка worst_case)

**Benchmark:**

![](/Users/astonuser/IdeaProjects/dev/java_lab1/imgs/worst_counting_sort_benchmark.png)

**CPU flamegraph:**

![](/Users/astonuser/IdeaProjects/dev/java_lab1/imgs/worst_counting_sort_cpu_flamegraph.png)

Видно, что большая часть времени алгоритма уходит на "доращивание" листа до нужных размеров при появлении числа, большего чем текущий размер листа(функция growListToSize). Так же видно, что немалая часть времени тратится на боксинг\анбоксинг инта(intValue + valueOf). И еще время уходит на операции листа (add, get, next).

**Memory allocation flamegraph:**

![](/Users/astonuser/IdeaProjects/dev/java_lab1/imgs/worst_counting_sort_mem_flamegraph.png)

Здесь видно, что основной расход памяти здесь так же вызван боксингом\анбоксингом и наращиванием листа. 

**CPU and Heap Memory**

![](/Users/astonuser/IdeaProjects/dev/java_lab1/imgs/worst_counting_sort_mem_cpu_usage.png)

Если посмотрим на график использования памяти\проца, то увидим что значения достигают 70% на проце и 1500 МБ по размеру кучи.

**Выводы**:
Для оптимизации попробуем заменить объект Integer примитивом int и динамический лист статическим массивом.

### Оптимизированный случай (ветка optimized)

**Benchmark:**

![optimized1_counting_sort_benchmark.png](imgs%2Foptimized1_counting_sort_benchmark.png)

Сразу видим, что после оптимизации throughput увеличился в 7 раз в сравнении с прошлым вариантом.

**CPU flamegraph:**

![optimized1_counting_sort_cpu_flamegraph.png](imgs%2Foptimized1_counting_sort_cpu_flamegraph.png)

В стеке теперь основное время расходует сам алгоритм, а конкретно итерирование по начальному массиву и нахождение максимума (см. скрин ниже).

![code_snippet_1.png](imgs%2Fcode_snippet_1.png)

**Memory allocation flamegraph:**

![optimized1_counting_sort_mem_flamegraph.png](imgs%2Foptimized1_counting_sort_mem_flamegraph.png)

В графе аллокации памяти на этот раз никакх сюрпризов - один раз выделяем память на вспомогательный массив, предусмотренный алгоритмом.

**CPU and Heap Memory**

![optimized1_counting_sort_mem_cpu_usage.png](imgs%2Foptimized1_counting_sort_mem_cpu_usage.png)

И по процу и по размеру кучи тоже, логично, получили значительный выигрыш по сравнению с прошлым вариантом: -30% проца и -400 МБ памяти

**Выводы**:
Так как у нас немалое время уходит на вспомогательный поиск максимумы, попробуем его оптимизировать, сделав обработку в стриме параллельной.

### Оптимизированный случай с параллельным поиском макс элемента (ветка optimized-parallel-max)

**Benchmark:**

![optimized2_counting_sort_benchmark.png](imgs%2Foptimized2_counting_sort_benchmark.png)

Throughput увеличился c 0.007 до 0.008

**CPU flamegraph:**

![optimized2_counting_sort_cpu_flamegraph.png](imgs%2Foptimized2_counting_sort_cpu_flamegraph.png)

Сама операция поиска максимума занимает  мало времени, но у нас появился оверхед за счет нескольких потоков и слияния их результатов - но даже с оверхедом получилось быстрее, чем при последовательной обработке.

**Memory allocation flamegraph:**

![optimized2_counting_sort_mem_flamegraph.png](imgs%2Foptimized2_counting_sort_mem_flamegraph.png)

В этом случае сэмпл памяти получился неудачный, но видно что общий объем аллоцированной памяти не изменился.

**CPU and Heap Memory**

![optimized1_counting_sort_mem_cpu_usage.png](imgs%2Foptimized1_counting_sort_mem_cpu_usage.png)

По сравнению с прошлой версией увеличилась загруженность проца(с 30 до 40%), а размер кучи остался таким же.

**Выводы**:
Попробуем теперь оптимизировать сам алгоритм подсчета элементов - уберем цикл, так как итерация по всем эдементам массива занимает большую часть времени.

### Оптимизированный случай с параллельным поиском макс элемента и подсчетом через параллельный стрим и AtomicIntegerArray(ветка optimized-parallel-max-atomic-array)

Альтернатива циклу - стрим, но в нашем случае надо инкрементировать внешний массив, что потокобезопасно с параллельным стримом сделать не получится, поэтому как вариант вместо обычного массива был выбран AtomicIntegerArray.

**Benchmark:**

![optimized3_counting_sort_benchmark.png](imgs%2Foptimized3_counting_sort_benchmark.png)

Throughput увеличился c 0.008 до 0.013 - относительно первого варианта throughput увеличился более, чем на порядок(!).

**CPU flamegraph:**

![optimized3_counting_sort_cpu_flamegraph.png](imgs%2Foptimized3_counting_sort_cpu_flamegraph.png)

Основное время, ожидаемо, занимает CAS в AtomicIntegerArray, но засчет паралельной обработки получилось получить выигрыш относительно обработки в цикле с 67% от общего времени до 11% (см. скрин ниже).

![code_snippet_2.png](imgs%2Fcode_snippet_2.png)

**Memory allocation flamegraph:**

![optimized3_counting_sort_mem_flamegraph.png](imgs%2Foptimized3_counting_sort_mem_flamegraph.png)

Здесь видим большой расход памяти на внутреннее содержимое AtomicIntegerArray(цена потокобезопасности).

**CPU and Heap Memory**

![optimized3_counting_sort_mem_cpu_usage.png](imgs%2Foptimized3_counting_sort_mem_cpu_usage.png)

По сравнению с прошлой версией увеличилась загруженность проца еще на 10-15 процентов(с 40 до 55%), а размер кучи остался таким же.

**Выводы**:
Получилось увеличить throughput с 0.001 до 0.013 и даже уменьшить размер кучи :)

## Блочная сортировка
### Худший случай (ветка worst_case)

Для начала попробуем с небольшим размером начальных данных посмотреть на зависимость скорости от количество блоков(корзин, карманов, ведер - ну вы поняли).

![worst_bucket_sort_buckets_amount_benchmark.png](imgs%2Fworst_bucket_sort_buckets_amount_benchmark.png)

Получили оптимальное количество бакетов = 1\5 от входного массива - в целом, логично - бакеты все еще маленькие и быстро сортируются, но при этом это не случай с размером бакетов = размеру входного массива, в котором алгоритм может деградировать до сортровки подсчетом, только с лишними операциями сортировки в бакетах.

Теперь проанализируем текущую реализацию с большими входными данными и размером бакетов = 1\5.

**Benchmark:**

![worst_bucket_sort_benchmark.png](imgs%2Fworst_bucket_sort_benchmark.png)

**CPU flamegraph:**

![worst_bucket_sort_cpu_flamegraph.png](imgs%2Fworst_bucket_sort_cpu_flamegraph.png)

Как и в случае с сортировкой подсчетом, в неоптимизированной версии видим большие затраты на работу с Integer вместо примитива, и так же завтраты на рост листа(ArrayList.grow).

**Memory allocation flamegraph:**

![worst_bucket_sort_mem_flamegraph.png](imgs%2Fworst_bucket_sort_mem_flamegraph.png)

По графу аллокации памяти также видны затраты на работу с листом.

**CPU and Heap Memory**

![worst_bucket_sort_mem_cpu_usage.png](imgs%2Fworst_bucket_sort_mem_cpu_usage.png)

Расход проца в текущей реализации в пиках достигает 60%, размер кучи 2500 МБ.

**Выводы**:

В отличие от сортировки подсчетом, блочная сортировка подразумевает динамическое добавление элементов, поэтому так просто от листа и объектов-оберток не избавиться - поэтому посмотрим по коду на что больше всего уходит времени (см. скрин ниже).

![code_snippet_3.png](imgs%2Fcode_snippet_3.png)

Во-первых, простая инициализация внешнего листа пустыми листами занимает очень много времени(вероятно из-за затрат на увеличение размера внешнего листа и времени на выделение памяти для внутренних листов), так же снова много времени уходит на итерирование по входным данным в цикле и на добавление нового элемента в корзину. Во-вторых, ожидаемо много времени уходит на сортировку корзин, но от этой сортировки нам не уйти, единственное что можно сделать - это распараллелить сортировку в разных корзинах.
Для оптимизации попробуем всю обработку сделать в потоке, для того чтоб не тратить время и память на создание и получение промежуточных структур(листов), а так же для удобного механизма параллелизма, предоставляемого Stream API.

### Оптимизированный случай с параллельной обработкой в стриме (ветка optimized-bucket-sort)

*Примечание - в данной ветке сразу рассматривается параллельный стрим, так как опыт оптимизации предыдущего алгоритма(и логика) подсказали что это скорее всего будет быстрее работать(были проведены эксперименты, подтвердившие гипотезу, результаты которых не приведены так как тема эффективности параллельности уже была рассмотрена с сортировкой подсчетом)*

**Benchmark:**

![optimized_bucket_sort_benchmark.png](imgs%2Foptimized_bucket_sort_benchmark.png)

Throughput увеличился в 10 раз - с 0.00001 до 0.0001.

**CPU flamegraph:**

![optimized_bucket_sort_cpu_flamegraph.png](imgs%2Foptimized_bucket_sort_cpu_flamegraph.png)

По графу видно что почти все время уходит на распределение элементов по бакетам(computeIfAbsent + collect) - если посмотрим ближе на эту строку в обработке, то увидим что collect занимает большую часть времени (см. скрин ниже).

![code_snippet_4.png](imgs%2Fcode_snippet_4.png)

**Memory allocation flamegraph:**

![optimized_bucket_sort_mem_flamegraph.png](imgs%2Foptimized_bucket_sort_mem_flamegraph.png)

По памяти ожидаеме видим самые большие расходны на groupingBy(распределение по бакетам) создания оберток для ключей мапы, из-за создания структур при группировании(листы) и из-за их роста(расходы на рост листа. 

**CPU and Heap Memory**

![optimized_bucket_sort_cpu_mem_usage.png](imgs%2Foptimized_bucket_sort_cpu_mem_usage.png)

Использования проца из-за параллелизма сильно выросло - с 60 до 90 процентов, по памяти в пиках значения остались примерно такими же - около 2500 МБ.

**Выводы**:
Так как тяжелее всего сборка элементов по бакетам, попробуем с ней что-нибудь сделать. Если зайти в джавадок метода groupingBy, то можно увидеть кое-что интересное - *The returned Collector is not concurrent. For parallel stream pipelines, the combiner function operates by merging the keys from one map into another, which can be an expensive operation. If preservation of the order in which elements appear in the resulting Map collector is not required, using groupingByConcurrent(Function) may offer better parallel performance.*
Нам порядок элементов в бакетах не важен, так что попробуем использовать **groupingByConcurrent**.

### Оптимизированный случай с параллельной обработкой в стриме (ветка optimized-bucket-sort-concurrentGroupingBy)

**Benchmark:**

![optimized2_bucket_sort_benchmark.png](imgs%2Foptimized2_bucket_sort_benchmark.png)

Throughput увеличился в 10 раз - с 0.0001 до 0.001.

**CPU flamegraph:**

![optimized2_bucket_sort_cpu_flamegraph.png](imgs%2Foptimized2_bucket_sort_cpu_flamegraph.png)

По графу видно что computeIfAbsent теперь занимает сильно меньшую часть относительно предыдущей версии. Так работает вероятно потому, что concurrentGroupingBy в отличие от groupingBy собирает результат в concurrentHashMap, а не в просто Map. Преимущество concurrentHashMap состоит в том что эта коллекция предназначена для многопоточной обработки - у нее критической секцией является бакет, а не вся коллекция, то есть несколько потоков могут работать с коллекций пока они работают с разными бакетами - что, соответственно, позволяет быстрее в многопоточной среде выполнять операцию group by.


**Memory allocation flamegraph:**

![optimized2_bucket_sort_mem_flamegraph.png](imgs%2Foptimized2_bucket_sort_mem_flamegraph.png)
 
По памяти картина примерно такая же, только нет прежнего расхода памяти на HashMap.merge.

**CPU and Heap Memory**

![optimized2_bucket_sort_cpu_mem_usage.png](imgs%2Foptimized2_bucket_sort_cpu_mem_usage.png)

Нагрузка на проц стала только чуть меньше - упара с 90 до 85 процентов, а вот размер кучи уменьшился значительнее - с 2500 до 2000 МБ.

**Выводы**:
За счет параллельных стримов получилось увеличить пропускную способность с 0.00001 до 0.001 операций в мс, что хорошо, но все равно сильно хуже чем итоговая сортировка подсчетом(по памяти, кстати, тоже). Из чего можно сделать выводы что расходы на использование коллекций в джаве слишком большие, так что если есть способ уйти от них (как получилось в сортировке подсчетом) - то это точно надо делать. 



