package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
//@RequiredArgsConstructor
public class BasicController {

    private final ItemRepository itemRepository;

    @Autowired
    public BasicController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public  String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

   // @PostMapping("/add")
    public String save(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam int quantity,
                       Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);

        return "basic/item";
    }


    //@PostMapping("/add")
    public String saveV2(@ModelAttribute("item") Item item/*, Model model*/){

        itemRepository.save(item);
        //model.addAttribute("item", item); //@ModelAttributr가 자동 생성, 따라서 Model도 주석처리 가능

        return "basic/item";
    }


    //@PostMapping("/add")
    public String saveV3(@ModelAttribute Item item){
        //class 명의 첫글자를 소문자로 바꿔 ex)Item->item 서 ModelAttribute에 담긴다.
        itemRepository.save(item);
        //model.addAttribute("item", item); //@ModelAttributr가 자동 생성한다.

        return "basic/item";
    }

    //@PostMapping("/add")
    public String saveV4(Item item){ //Item -> item 으로 ModelAttribute에 담긴다.
        itemRepository.save(item);
        return "basic/item";
        //PRG pattern 적용 X
    }

    //@PostMapping("/add")
    public String saveV5(Item item){ //Item -> item 으로 ModelAttribute에 담긴다.
        itemRepository.save(item);
        //PRG pattern 적용 O
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String saveV6(Item item, RedirectAttributes redirectAttributes){ //Item -> item 으로 ModelAttribute에 담긴다.
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); //?status=true

        //PRG pattern 적용 O
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    // test용 데이터 추가
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

    }
}
